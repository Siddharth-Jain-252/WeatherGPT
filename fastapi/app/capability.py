from app.enums import UserSpeciality


CAPABILITIES: dict[UserSpeciality, set[str]] = {

    UserSpeciality.NORMAL_USER: {
        "current_weather_data",
        "day_weather_data",
        "week_weather_data",
    },

    UserSpeciality.MARINE: {
        "current_weather_data",
        "day_weather_data",
        "week_weather_data",
        "marine_weather_data",
    },

    UserSpeciality.AVIATION: {
        "current_weather_data",
        "day_weather_data",
        "week_weather_data",
        "flight_details",
        "aviation_weather_data",
    },

    UserSpeciality.AGRICULTURE: {
        "current_weather_data",
        "day_weather_data",
        "week_weather_data",
    },

    UserSpeciality.RESEARCH: {
        "current_weather_data",
        "day_weather_data",
        "week_weather_data",
        "historical_weather_data",
    },
}


class RoleService:

    @staticmethod
    def get_capabilities(
        speciality: UserSpeciality,
    ) -> set[str]:

        return CAPABILITIES.get(
            speciality,
            set()
        )

    @staticmethod
    def is_allowed(
        speciality: UserSpeciality,
        capability: str,
    ) -> bool:

        return capability in RoleService.get_capabilities(
            speciality
        )

    @staticmethod
    def validate_capabilities(
        speciality: UserSpeciality,
        requested_capabilities: list[str],
    ) -> list[str]:

        allowed = RoleService.get_capabilities(speciality)

        return [
            capability
            for capability in requested_capabilities
            if capability in allowed
        ]