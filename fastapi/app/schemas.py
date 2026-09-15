from typing import Any
from pydantic import BaseModel, Field
from app.enums import UserSpeciality

class Message(BaseModel):
    user_message: str
    llm_message: str

class ChatIntentRequest(BaseModel):
    speciality: UserSpeciality
    past_messages: list[Message] = Field(default_factory=list)
    query: str = Field(min_length=1)

class ChatIntentResponse(BaseModel):
    intent: str
    location: str | None
    required_capabilities: list[str]

class ChatRequest(BaseModel):
    speciality: UserSpeciality
    intent: str
    query: str
    data: dict[str, Any] = Field(default_factory=dict)

class ChatResponse(BaseModel):
    response: str

class AlertRequest(BaseModel):
    speciality: UserSpeciality
    alert: str = Field(min_length=1)
