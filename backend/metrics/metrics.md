

# 1. First: what you already have (GOOD BASE)

You already correctly enabled:

### Per service (both gateway + user)

* Micrometer Prometheus registry ✔
* Actuator `/prometheus` ✔
* HTTP metrics ✔
* JVM metrics ✔

### Gateway (important extra)

* Circuit Breaker (Resilience4j) ✔
* Retry filter ✔
* Load balancing ✔

So we already have **rich telemetry**.

---

# 2. What we should measure (design first)

We split into:

# A) GLOBAL DASHBOARD (ALL SERVICES)

This is your “system health view”.

We will use only 6–8 core metrics:

## 1. Service availability

```promql
up
```

👉 Shows if services are alive

---

## 2. Total request rate (all services)

```promql
sum(rate(http_server_requests_seconds_count[1m])) by (application)
```

---

## 3. Error rate (5xx)

```promql
sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) by (application)
```

---

## 4. Latency (avg)

```promql
sum(rate(http_server_requests_seconds_sum[1m]))
/
sum(rate(http_server_requests_seconds_count[1m]))
```

---

## 5. JVM memory (heap)

```promql
jvm_memory_used_bytes{area="heap"}
```

---

## 6. CPU usage

```promql
process_cpu_usage
```

---

## 7. Active requests

```promql
http_server_requests_active_seconds_count
```

---

# B) PER-SERVICE DASHBOARD (IMPORTANT)

We will use label:

```text
application
```

or sometimes:

```text
job
```

So first we should confirm labels exist:

👉 In Grafana Explore run:

```promql
http_server_requests_seconds_count
```

Look for labels like:

* `job`
* `instance`
* `application`

---

## USER SERVICE DASHBOARD

### 1. Request rate

```promql
rate(http_server_requests_seconds_count{job="user-service"}[1m])
```

---

### 2. Latency

```promql
rate(http_server_requests_seconds_sum{job="user-service"}[1m])
/
rate(http_server_requests_seconds_count{job="user-service"}[1m])
```

---

### 3. Error rate

```promql
rate(http_server_requests_seconds_count{job="user-service", status=~"5.."}[5m])
```

---

### 4. JVM memory

```promql
jvm_memory_used_bytes{job="user-service", area="heap"}
```

---

# GATEWAY DASHBOARD (VERY IMPORTANT)

This is where microservices debugging happens.

---

## 1. Request rate per route

```promql
rate(http_server_requests_seconds_count{job="gateway-service"}[1m])
```

---

## 2. Gateway errors

```promql
rate(http_server_requests_seconds_count{job="gateway-service", status=~"5.."}[5m])
```

---

## 3. Circuit breaker state (VERY IMPORTANT)

Resilience4j exposes:

```promql
resilience4j_circuitbreaker_state
```

---

## 4. Retry count

```promql
resilience4j_retry_calls_total
```

---

## 5. Downstream latency impact

```promql
rate(http_server_requests_seconds_sum{job="gateway-service"}[1m])
/
rate(http_server_requests_seconds_count{job="gateway-service"}[1m])
```

---

# 3. IMPORTANT DESIGN ISSUE IN YOUR CONFIG

Right now you DID NOT explicitly set:

```yaml
spring.application.name
```

in a way Prometheus can use consistently.

👉 This matters because dashboards rely on labels.

---

# 4. RECOMMENDED FIX (VERY IMPORTANT)

Add this to BOTH services:

## user-service

```yaml
management:
  metrics:
    tags:
      application: user-service
```

## gateway-service

```yaml
management:
  metrics:
    tags:
      application: gateway-service
```

---

### Why this is critical

Without this:

* Grafana queries become messy
* You depend on `job` or `instance`
* Multi-instance scaling breaks dashboards

With this:
👉 Everything becomes clean:

```promql
{application="user-service"}
```

---

# 5. FINAL DASHBOARD STRUCTURE (WHAT WE WILL BUILD)

## Dashboard 1: SYSTEM OVERVIEW

* All services UP/DOWN
* Total traffic
* Error rate
* Global latency
* JVM memory (all services)

---

## Dashboard 2: USER SERVICE

* Requests/sec
* Latency
* Errors
* JVM memory
* CPU

---

## Dashboard 3: GATEWAY

* Requests/sec
* Retry count
* Circuit breaker state
* Route latency
* Error rate

---

## Dashboard 4: MICROSERVICE MAP (advanced later)

* service-to-service traffic
* dependency latency

---

# 6. NEXT STEP (IMPORTANT)

Before we build Grafana panels, do this:

### 👉 Tell me:

Run in Grafana Explore:

```promql
http_server_requests_seconds_count
```

and paste ONE sample result showing labels like:

* job
* instance
* uri
* status


