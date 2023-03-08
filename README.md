# saga-choreography-example

![](.README_images/architecture.png)

# Apache Kafka
https://hub.docker.com/r/bitnami/kafka
docker-compose
```yaml
version: "3"
services:
  zookeeper:
    image: 'bitnami/zookeeper:latest'
    ports:
      - '2181:2181'
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    image: 'bitnami/kafka:latest'
    ports:
      - '9092:9092'
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
    depends_on:
      - zookeeper
```

run: docker-compose up

##
Kafka tool: https://www.kafkatool.com/

- with this tool we can monitor messages in the topics.
- install the tool and connect to the kafka cluster


## Run the microservices:
- order service
- payment service

Now we can test the saga pattern:

## request
```bash
curl --location --request POST 'http://localhost:8081/order/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId": 103,
    "productId": 33,
    "amount": 4000
}'
```
## Kafka payload

### Happy scenario
```bash
{"eventId":"b0e47448-eeb8-4cf4-bd29-b3a4315fc592","date":"2021-09-03T17:26:46.777+00:00","orderRequestDto":{"userId":103,"productId":33,"amount":4000,"orderId":1},"orderStatus":"ORDER_CREATED"}
```

```bash
{"eventId":"c48c5593-9f81-4ab4-9de8-b9fca2d2bef2","date":"2021-09-03T17:26:51.989+00:00","paymentRequestDto":{"orderId":1,"userId":103,"amount":4000},"paymentStatus":"PAYMENT_COMPLETED"}
```

## Request
```bash
curl --location --request POST 'http://localhost:8081/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId": 103,
    "productId": 12,
    "amount": 800
}'
```

### insufficent amount
```bash
{"eventId":"fecacc77-017d-49cd-bdfa-58e47170da49","date":"2021-09-03T17:28:23.126+00:00","orderRequestDto":{"userId":103,"productId":12,"amount":800,"orderId":2},"orderStatus":"ORDER_CANCELLED"}
```

```bash
{"eventId":"46378bbc-5d15-4436-bed1-c6f3ddb1dc31","date":"2021-09-03T17:28:15.940+00:00","paymentRequestDto":{"orderId":2,"userId":103,"amount":800},"paymentStatus":"PAYMENT_FAILED"}
```

```bash
curl --location --request GET 'http://localhost:8081/orders' \
--header 'Content-Type: application/json' \
--data-raw ''
```

## Response

```bash
[
    {
        "id": 1,
        "userId": 103,
        "productId": 33,
        "price": 4000,
        "orderStatus": "ORDER_COMPLETED",
        "paymentStatus": "PAYMENT_COMPLETED"
    },
    {
        "id": 2,
        "userId": 103,
        "productId": 12,
        "price": 800,
        "orderStatus": "ORDER_CANCELLED",
        "paymentStatus": "PAYMENT_FAILED"
    }
]
```

