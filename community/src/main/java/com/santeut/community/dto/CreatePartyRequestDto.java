package com.santeut.community.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class CreatePartyRequestDto {

    public String schedule;
    public String partyName;
    public int mountainId;
    public String mountainName;
    public int maxPeople;
    public Integer guildId;
    public String place;
    public int[] selectedCourse;

    @Override
    public String toString() {
        return String.format("schedule=%s, partyName=%s, mountainName=%s, maxPeople=%d, guildId=%d, place=%s",
                schedule, partyName, mountainName, maxPeople, guildId, place)+" "+ Arrays.toString(selectedCourse);
    }

}
