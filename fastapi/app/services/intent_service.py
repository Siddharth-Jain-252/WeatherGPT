import json
from app.capability import RoleService
from app.enums import UserSpeciality
from app.llm_service import chat_with_llm

def detect_intent(speciality: UserSpeciality, query: str, past_messages: list) -> dict:
    allowed_capabilities = sorted(RoleService.get_capabilities(speciality))
    system_prompt = """
You are the intent classifier for WeatherGPT.

Return JSON only:
{"intent":"string","location":"string or null","required_capabilities":["capability"]}

LOCATION:
1. Prefer an explicit location in the current query.
2. Otherwise use a location established in previous messages.
3. If no location is available, return null.
4. Do not invent a location.

CAPABILITIES:
1. Use only supplied allowed capabilities.
2. Return [] when no backend capability is needed.
3. Never invent capability names.
4. For weather-dependent advice, request the minimum relevant weather capability.
5. Always return an array for required_capabilities.

Example:
Previous: "I live in Mumbai"
Current: "Should I go to the market?"
Return location "Mumbai" and capability "current_weather_data".
"""
    user_prompt = f"""
User speciality:
{speciality.value}

Allowed capabilities:
{json.dumps(allowed_capabilities)}

Previous conversation:
{json.dumps([message.model_dump() for message in past_messages], ensure_ascii=False)}

Current user query:
{query}
"""
    answer = chat_with_llm(
        [{"role":"system","content":system_prompt},{"role":"user","content":user_prompt}],
        temperature=0, response_format={"type":"json_object"})
    try:
        result = json.loads(answer)
    except json.JSONDecodeError as exc:
        raise ValueError("LLM returned invalid intent JSON") from exc

    intent = result.get("intent")
    location = result.get("location")
    requested = result.get("required_capabilities", [])
    if not isinstance(intent, str) or not intent.strip(): raise ValueError("LLM returned an invalid intent")
    if location is not None and not isinstance(location, str): location = None
    if not isinstance(requested, list): raise ValueError("required_capabilities must be a list")
    validated = RoleService.validate_capabilities(speciality, requested)
    return {"intent": intent.strip(),
            "location": location.strip() if isinstance(location,str) and location.strip() else None,
            "required_capabilities": validated}
