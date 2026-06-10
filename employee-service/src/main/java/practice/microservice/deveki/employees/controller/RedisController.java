package practice.microservice.deveki.employees.controller;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.SetParams;

@RestController
public class RedisController {
	
	@Value("${redis.host:localhost}")
	private String redisHost;
	
	@Value("${redis.port:6379}")
	private String redisPort;
	
	@GetMapping("/redis/ping")
	public ResponseEntity<String> pingRedis() {
		try (Jedis jedis = new Jedis(redisHost, Integer.parseInt(redisPort))) {
		    // Actively ping the server
		    String response = jedis.ping();
		    
		    if ("PONG".equals(response)) {
		        System.out.println("Redis is up and running!");
		        return ResponseEntity.ok(response);
		    }
		    return ResponseEntity.internalServerError().body("Redis server is not running.");
		} catch (JedisConnectionException e) {
		    System.err.println("Could not connect to Redis: " + e.getMessage());
		    return ResponseEntity.internalServerError().body("Unable to connect to Redis server.");
		}

	}

	@PostMapping("/redis/{name}")
	public String saveData(@PathVariable("name") String name) {
		HashMap<String, String> data = new HashMap<>();
		ResponseEntity<String> redisServerStatus = pingRedis();
		if(redisServerStatus.getStatusCode() == HttpStatusCode.valueOf(500)) 
		{
			data.put("status", "failed");
			data.put("message", redisServerStatus.getBody());
			return new ObjectMapper().writeValueAsString(data);
		}
		String requestId = save(name);
		if(requestId == null) 
		{
			data.put("status", "failed");
			data.put("message", "Unable to save data in redis.");
		}else 
		{
			data.put("status", "success");
			data.put("name", name);
			data.put("requestId", requestId);
			data.put("message", "Data saved successfully in redis.");
		}
		return new ObjectMapper().writeValueAsString(data);
	}
	
	@GetMapping("/redis/{id}/{name}")
	public String deleteData(@PathVariable("id") String requestId, @PathVariable("name") String name) {
		HashMap<String, String> data = new HashMap<>();
		Jedis jedis = new Jedis(redisHost, Integer.parseInt(redisPort));
		boolean deleted = releaseLock(getKey(name),requestId, jedis);
		if(!deleted) 
		{
			data.put("status", "failed");
			data.put("message", "Unable to delete data from redis.");
		}else 
		{
			data.put("status", "success");
			data.put("message", "Data '"+name+"' deleted successfully from redis.");
		}
		return new ObjectMapper().writeValueAsString(data);
	}

	private String save(String name) {
		Jedis jedis = new Jedis(redisHost, Integer.parseInt(redisPort));
		String key = "users_name_"+name;
		return acquireLock(key, 60, jedis);
	}
	
	private String getKey(String name) 
	{
		return "users_name_"+name;
	}

	// Acquire Lock
	public String acquireLock(String lockKey, int expirySeconds, Jedis jedis) {
		String requestId = UUID.randomUUID().toString();

		SetParams params = new SetParams();
		params.nx().ex(expirySeconds); // NX = only if not exists, EX = expiry

		String result = jedis.set(lockKey, requestId, params);

		if ("OK".equals(result)) {
			return requestId; // Lock acquired
		}
		return null; // Failed
	}

	// Release Lock safely
	public boolean releaseLock(String lockKey, String requestId, Jedis jedis) {

		// Lua script ensures only owner releases lock
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " + "   return redis.call('del', KEYS[1]) "
				+ "else " + "   return 0 " + "end";

		Object result = jedis.eval(script, 1, lockKey, requestId);

		return Long.valueOf(1).equals(result);
	}

}
