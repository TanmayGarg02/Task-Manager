# 🌱 Reewild – Food Carbon Emission Estimator  

This project is a **Spring Boot application** that estimates the **carbon footprint of food dishes**.  
It integrates with **LLM (Large Language Models)** for ingredient extraction and **Vision AI** for recognizing dishes from images.  

---

## 🚀 How to Run  

### Locally  
1. Clone the repo:  
   ```bash
   git clone https://github.com/your-username/reewild.git
   cd reewild
   ```
2. Configure environment variables in `application.yml` or `application.properties`:  
   ```yaml
   openai:
     api-key: your_api_key_here
     base-url: https://api.openai.com/v1
     chat-model: gpt-4o-mini
     enabled: true

   vision:
     api-key: your_api_key_here
     base-url: https://vision.api.com
     enabled: true
   ```
3. Run the app:  
   ```bash
   ./mvnw spring-boot:run
   ```

### With Docker  
```bash
docker build -t reewild .
docker run -p 8080:8080 reewild
```

---

## 📡 Example Requests & Responses  

### 1. Estimate carbon footprint from text dish name  
**Request:**  
```http
POST /api/estimate
Content-Type: application/json

{
  "dish": "Paneer Butter Masala",
  "servings": 2
}
```

**Response:**  
```json
{
  "dish": "Paneer Butter Masala",
  "ingredients": ["paneer", "butter", "tomato", "cream", "spices"],
  "carbonEmissionKg": 3.8,
  "servings": 2,
  "totalCarbonEmissionKg": 7.6
}
```

---

### 2. Estimate carbon footprint from image  
**Request:**  
```http
POST /api/estimate/image
Content-Type: multipart/form-data

file=@dish.jpg
servings=1
```

**Response:**  
```json
{
  "dish": "Cheeseburger",
  "ingredients": ["beef patty", "cheese", "bun", "lettuce", "tomato"],
  "carbonEmissionKg": 5.2,
  "servings": 1,
  "totalCarbonEmissionKg": 5.2
}
```

---

## 📌 Assumptions & Limitations  
- Ingredient extraction depends on **LLM accuracy** (OpenAI GPT).  
- Vision recognition may misclassify dishes in low-quality images.  
- Carbon emission values are estimated based on a static dataset, not real-time data.  
- **Scaling emissions by servings** is linear (no cooking efficiency considered).  

---

## 🛠 Design Decisions  
- **LLM Client (`LLMClient`)**: Extracts ingredients from dish name using OpenAI Chat API.  
- **Vision Client (`VisionClient`)**: Identifies dish from images, returns label for further LLM processing.  
- **Service Layer**: Orchestrates clients and multiplies results with servings.  
- **DTO (`FoodEstimate`)**: Acts as request + response model.  

Reasoning: Separation of concerns makes it easy to swap providers (OpenAI, Gemini, Mock clients).  

---

## 🚧 Production Considerations  
If moving to production:  
- Add **API rate limiting** to control OpenAI/vision costs.  
- Implement **persistent caching** for repeated dishes.  
- Add **monitoring & logging** (Micrometer, Prometheus).  
- Secure APIs with **OAuth2 / JWT**.  
- Scale using **Kubernetes + horizontal pod autoscaling**.  

---

## ✨ Bonus (Optional Extensions)  
- [ ] Dockerized build for deployment.  
- [ ] Swagger/OpenAPI for interactive API docs.  
- [ ] Basic authentication.  
- [ ] Unit & integration tests (MockMvc + Mockito).  
- [ ] Cloud deployment (Render / AWS / Azure / GCP).  

---

📂 **Repo Structure**  
```
reewild/
 ┣ src/main/java/com/example/reewild
 ┃ ┣ config/          # Config classes (API keys, props)
 ┃ ┣ controller/      # REST controllers
 ┃ ┣ llm/             # LLM client + OpenAI impl
 ┃ ┣ vision/          # Vision client + impl
 ┃ ┣ service/         # Core service logic
 ┃ ┗ model/           # DTOs (FoodEstimate)
 ┣ src/test/java/...  # Unit & integration tests
 ┣ Dockerfile
 ┣ README.md
 ┗ pom.xml
```
