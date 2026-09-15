from enum import Enum


class UserSpeciality(str, Enum):
    NORMAL_USER = "normal_user"
    MARINE = "marine"
    AVIATION = "aviation"
    AGRICULTURE = "agriculture"
    RESEARCH = "research"