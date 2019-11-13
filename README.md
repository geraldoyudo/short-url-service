# Short Url Service

## Requirements

- Docker
- Docker Compose
- Maven and jdk 11 (to build)

## Getting Started

- Pull source code to directory (let the directory be 'short-url-service')
- `cd short-url-service/docker`
- `docker-compose up`

## Configuration

Configuring short url service can be done by modifying the `docker/docker-compose.yml` file and 
updating the environment variables.

      - REDIS_HOST=redis (redis host)
      - REDIS_PORT=6379 (redis port)
      - BASE_URL=http://localhost:9095 (base url for the short urls)
      
## API

### Creating a short url

This can be done with a simple POST request as shown below:

```
POST  HTTP/1.1
Host: localhost:9095
Accept: */*

{
	"url": "http://google.com/something-really-long",
	"duration": {
		"value": 1000000,
		"unit": "SECONDS"
	}
}
```

It results in the following:

```
{
    "shortUrl": "http://localhost:9095/s/mz7ssX"
}
```

### Using the short url

Just copy and paste the link in the `shortUrl` property of the response. You will be redirected to your URL.
