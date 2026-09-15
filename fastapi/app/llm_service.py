from groq import Groq

from app.config import GROQ_API_KEY, MODEL_NAME


client = Groq(
    api_key=GROQ_API_KEY
)


def chat_with_llm(
    messages: list[dict[str, str]],
    *,
    temperature: float = 0.2,
    response_format: dict | None = None,
) -> str:

    request = {
        "model": MODEL_NAME,
        "messages": messages,
        "temperature": temperature,
    }

    if response_format is not None:
        request["response_format"] = response_format

    response = client.chat.completions.create(
        **request
    )

    content = response.choices[0].message.content

    if not content:
        raise RuntimeError(
            "LLM returned an empty response"
        )

    return content