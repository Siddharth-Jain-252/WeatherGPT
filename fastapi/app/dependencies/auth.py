from fastapi import HTTPException, Security, status
from fastapi.security import APIKeyHeader
from app.config import FASTAPI_API_KEY
api_key_header = APIKeyHeader(name="api_key", auto_error=False)
async def verify_api_key(api_key: str = Security(api_key_header)):
    if not api_key or api_key != FASTAPI_API_KEY:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid or missing API key")
    return api_key
