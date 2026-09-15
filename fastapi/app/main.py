from fastapi import Depends, FastAPI, HTTPException
from app.dependencies.auth import verify_api_key
from app.schemas import AlertRequest, ChatIntentRequest, ChatIntentResponse, ChatRequest, ChatResponse
from app.services.intent_service import detect_intent
from app.llm_service import chat_with_llm

app = FastAPI(title="WeatherGPT LLM Service", description="Internal role-aware LLM service", version="3.0.0")

@app.get("/health")
def health(): return {"status":"ok"}

@app.get("/")
def root(api_key: str = Depends(verify_api_key)):
    return {"message":"WeatherGPT LLM service is running"}

@app.post("/chat/intent", response_model=ChatIntentResponse)
def chat_intent(request: ChatIntentRequest, api_key: str = Depends(verify_api_key)):
    try: return detect_intent(request.speciality, request.query, request.past_messages)
    except ValueError as exc: raise HTTPException(status_code=400, detail=str(exc)) from exc
    except Exception as exc: raise HTTPException(status_code=500, detail="Failed to determine user intent") from exc

@app.post("/chat", response_model=ChatResponse)
def chat(request: ChatRequest, api_key: str = Depends(verify_api_key)):
    system_prompt = """
You are WeatherGPT, a weather information assistant.
Use ONLY backend data supplied in the request.
Never invent weather values, locations, forecasts, or API results.
If required data is missing or null, say that the requested data is unavailable.
Be concise, clear, conversational, and useful.
Respect the user's speciality and detected intent.
"""
    user_prompt = f"""User speciality: {request.speciality.value}
Detected intent: {request.intent}
User query: {request.query}
Backend data: {request.data}
"""
    try:
        answer = chat_with_llm(
            [{"role":"system","content":system_prompt},{"role":"user","content":user_prompt}],
            temperature=0)
        return ChatResponse(response=answer)
    except Exception as exc: raise HTTPException(status_code=500, detail="Failed to generate response") from exc

@app.post("/chat/alert", response_model=ChatResponse)
def chat_alert(request: AlertRequest, api_key: str = Depends(verify_api_key)):
    try:
        answer = chat_with_llm(
            [{"role":"system","content":"You are WeatherGPT. Generate a concise customised weather alert using only the supplied alert. Do not invent facts."},
             {"role":"user","content":f"User speciality: {request.speciality.value}\nAlert: {request.alert}"}],
            temperature=0)
        return ChatResponse(response=answer)
    except Exception as exc: raise HTTPException(status_code=500, detail="Failed to generate alert") from exc
