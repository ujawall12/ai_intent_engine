# 🧠 AI Intent Engine — Spring Boot + Spring AI

> A production-grade AI-powered backend agent that understands natural language and converts it into real backend actions.
> No chatbots. No hardcoded if-else chains. Real backend engineering.

Java
Spring Boot
Spring AI
PostgreSQL
Docker
License

---

## 📺 Watch The Full Build

This project was built live on the **SpringMinds** YouTube channel.

> **[Watch the video → SpringMinds on YouTube](#)**

If you find this useful please ⭐ star the repo and subscribe to the channel. It helps more Java developers find this content.

---

## 🎯 What Is This?

Traditional backend APIs expect structured input like this:

```json
POST /track-order
{
  "orderId": "ORD-12345"
}
```

But real users say things like:

- *"I ordered shoes last week but haven't received them"*
- *"where is my stuff"*
- *"I want my money back for the jacket"*
- *"track my last order"*

This project solves that gap. Instead of forcing users to adapt to your API, you let AI adapt to users.

**Spring AI extracts the intent and entities from natural language. Your backend stays exactly the same. Your database stays exactly the same. AI is purely the translation layer.**

---

## 🏗️ Architecture

```
User Natural Language Input
           ↓
   Spring Boot REST API
           ↓
   Spring AI — Intent Extraction
   (GPT-4o-mini via OpenAI)
           ↓
      UserIntent POJO
   {
     intent: "TRACK_ORDER"
     productName: "shoes"
     timeframe: "last week"
     confidence: 0.94
   }
           ↓
   Order Routing Service
           ↓
   PostgreSQL Database Query
           ↓
   Real Order Data
           ↓
   Structured API Response
```

---

## ✨ Key Features

- **Natural Language Understanding** — Accepts any user phrasing and extracts structured intent using Spring AI
- **Structured Output Conversion** — Uses Spring AI's `StructuredOutputConverter` to map AI responses directly to Java POJOs
- **Real Database Integration** — Queries actual PostgreSQL data based on extracted entities
- **Confidence Thresholds** — Low confidence responses trigger clarification requests instead of guessing wrong
- **Production-Grade Architecture** — SOLID principles, interface-based design, centralized exception handling
- **Docker Compose Setup** — One command to start all infrastructure
- **Input Validation** — Request validation using Spring's @Valid and @NotBlank
- **Health Monitoring** — Spring Boot Actuator endpoints included

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 4.0.5 | Framework |
| Spring AI | 2.0.0-M4 | AI Integration |
| OpenAI GPT-4o-mini | Latest | Language Model |
| Spring Data JPA | Managed by Boot | Database ORM |
| PostgreSQL | 16 | Database |
| Docker Compose | Latest | Infrastructure |
| Lombok | Managed by Boot | Boilerplate Reduction |
| Spring Validation | Managed by Boot | Input Validation |
| Spring Actuator | Managed by Boot | Health Monitoring |

---

## 📁 Project Structure

```
com.springminds.aiintentengine
│
├── config/
│   ├── ChatClientConfig.java        # Spring AI ChatClient bean
│   └── DataSeeder.java              # Seeds demo data on startup
│
├── controller/
│   └── OrderQueryController.java    # REST endpoint — HTTP only
│
├── service/
│   ├── IntentExtractionService.java # Interface
│   ├── OrderRoutingService.java     # Interface
│   └── impl/
│       ├── IntentExtractionServiceImpl.java  # AI layer
│       └── OrderRoutingServiceImpl.java      # Business logic
│
├── repository/
│   └── OrderRepository.java         # JPA repository
│
├── entity/
│   └── Order.java                   # JPA entity
│
├── model/
│   ├── request/
│   │   └── UserQueryRequest.java    # Incoming DTO
│   ├── response/
│   │   └── ApiResponse.java         # Outgoing DTO
│   └── intent/
│       └── UserIntent.java          # AI structured output model
│
├── exception/
│   ├── IntentExtractionException.java
│   ├── OrderNotFoundException.java
│   └── GlobalExceptionHandler.java  # Centralized error handling
│
└── constant/
    └── IntentConstants.java         # No magic strings
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have these installed:

- Java 21+
- Maven 3.9+
- Docker Desktop
- An OpenAI API account with credits

### Step 1 — Clone The Repository

```bash
git clone https://github.com/ujawall12/ai_intent_engine.git
cd ai-intent-engine
```

### Step 2 — Start PostgreSQL With Docker

```bash
docker-compose up -d
```

This starts a PostgreSQL 16 container with:
- Database: `intentdb`
- Username: `springminds`
- Password: `springminds123`
- Port: `5432`

Verify it is running:
```bash
docker ps
```

### Step 3 — Set Your OpenAI API Key

Get your API key from platform.openai.com then set it as an environment variable.

**In IntelliJ:**
Run → Edit Configurations → Environment Variables → Add:
```
OPENAI_API_KEY=your_api_key_here
```

### Step 4 — Run The Application

```bash
mvn spring-boot:run
```

Or run `AiIntentEngineApplication.java` directly from IntelliJ.

On startup you will see:

```
DEBUG - Database seeded with 4 orders
INFO  - Started AiIntentEngineApplication
```

### Step 5 — Test With Postman

Import the Postman collection from `/postman/SpringMinds-AI-Intent-Engine.json`

Or create a manual request:

```
POST http://localhost:8080/api/v1/ai-query
Content-Type: application/json

{
  "message": "I ordered shoes last week but haven't received them"
}
```

---

## 📬 API Reference

### POST /api/v1/ai-query

Accepts natural language input and returns a structured response.

**Request:**
```json
{
  "message": "string (3-500 characters, required)"
}
```

**Response — Success:**
```json
{
  "success": true,
  "intent": "TRACK_ORDER",
  "confidence": 0.94,
  "message": "Your order is on the way! Tracking number: TRK-789456123. Expected delivery: 2026-04-21",
  "data": {
    "orderId": "ORD-001",
    "productName": "Nike Running Shoes",
    "status": "IN_TRANSIT",
    "orderDate": "2026-04-13",
    "estimatedDelivery": "2026-04-21",
    "amount": "₹4999.0",
    "trackingNumber": "TRK-789456123"
  }
}
```

**Response — Low Confidence:**
```json
{
  "success": false,
  "intent": "UNCLEAR",
  "confidence": 0.0,
  "message": "I am not sure what you are asking. Could you please provide more details about your order?",
  "data": null
}
```

**Supported Intents:**

| Intent | Example Inputs |
|---|---|
| TRACK_ORDER | "where is my order", "I haven't received my shoes" |
| CANCEL_ORDER | "cancel my order", "I don't want the jacket anymore" |
| REFUND_REQUEST | "I want my money back", "give me a refund" |
| GENERAL_INQUIRY | "what orders do I have", "show my purchases" |

---

## 🧪 Test Scenarios

Try these inputs in Postman to see the full range of capabilities:

```json
{ "message": "I ordered shoes last week but haven't received them" }
{ "message": "where is my stuff" }
{ "message": "track my last order" }
{ "message": "I want to cancel my jacket order" }
{ "message": "I didn't get my sneakers" }
{ "message": "give me my money back for the adidas" }
{ "message": "what orders do I have" }
{ "message": "hi" }
```

---

## ⚙️ Configuration Reference

```properties
# OpenAI
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.1
spring.ai.openai.chat.options.max-tokens=500

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/intentdb
spring.datasource.username=springminds
spring.datasource.password=springminds123

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

**Why temperature=0.1?**
Low temperature means more deterministic and consistent JSON output from the AI. For intent extraction you want precision not creativity.

---

## 🤝 Contributing

Contributions are welcome. Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**SpringMinds**
Building real AI backend systems with Java.

- YouTube: [SpringMinds](#)
- GitHub: [SpringMinds](#)

---

## ⭐ Support

If this project helped you please:

- ⭐ Star this repository
- 📺 Subscribe to SpringMinds on YouTube
- 🔔 Turn on notifications for the next video in this series

Every star and subscription helps more Java developers find this content.

---

*Built with Spring Boot, Spring AI, and a refusal to write if-else chains for natural language processing.*
