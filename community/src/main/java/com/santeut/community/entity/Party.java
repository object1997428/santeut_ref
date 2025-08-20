package com.santeut.community.entity;

import com.santeut.community.dto.CreatePartyRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "party")
public class Party extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "party_id", nullable = false)
        private int partyId;

        @Column(name = "party_is_linked", nullable = false)
        private boolean isLinked;

        @Column(name = "course_id", nullable = false)
        private int courseId;

        @Column(name = "courses")
        private String selectedCourse;

        @Column(name = "guild_id")
        private Integer guildId;

        @Column(name = "user_id", nullable = false)
        private int userId;

        @Column(name = "party_mountain_name", length = 300, nullable = false)
        private String mountainName;

        @Column(name = "mountain_id", nullable = false)
        private int mountainId;

        @Column(name = "party_name", columnDefinition = "TEXT",nullable = false)
        private String partyName;

        @Column(name = "party_schedule", nullable = false)
        private LocalDateTime schedule;

        @Column(name = "party_place", columnDefinition = "TEXT",nullable = false)
        private String place;

        @Column(name = "party_status", length = 1, nullable = false)
        private char status;

        @Column(name = "party_max_participants", nullable = false)
        private int maxParticipants;

        @Column(name = "party_participants", nullable = false)
        private int participants;

        @Column(name = "party_started_at")
        private LocalDateTime started_at;

        @Column(name = "party_finished_at")
        private LocalDateTime finished_at;

        public static Party createEntity(int userId, CreatePartyRequestDto requestDto, String selectedCourse) {
            return Party.builder()
                    .userId(userId)
                    .isLinked(requestDto.getGuildId() != null)
                    .selectedCourse(selectedCourse)
                    .guildId(requestDto.getGuildId())
                    .mountainId(requestDto.getMountainId())
                    .mountainName(requestDto.getMountainName())
                    .partyName(requestDto.getPartyName())
                    .schedule(LocalDateTime.parse(requestDto.getSchedule(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .place(requestDto.getPlace())
                    .status('B')
                    .maxParticipants(requestDto.getMaxPeople())
                    .participants(0)
                    .build();
        }

        public void deleteParty(Character status) {
            this.setDeleted(true);
            this.status = status;
        }

        public void modifyPartyInfo(String partyName, String schedule, String place, int maxParticipants) {
            this.partyName = partyName;
            this.schedule = LocalDateTime.parse(schedule, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.place = place;
            this.maxParticipants = maxParticipants;
        }

        public synchronized boolean addParticipant() {
            if(this.participants>=this.maxParticipants) {
                return false;
            }
            this.participants += 1;
            return true;
        }

        public synchronized void minusParticipant() {
            this.participants -= 1;
        }

        /** 비즈니스 로직 **/
        public boolean canPartyStart(){
            return !this.schedule.isAfter(LocalDateTime.now());
        }


        public void setPartyStatus(char status){
            if(status=='P'){
                this.started_at=LocalDateTime.now();
            }
            else if(status=='E'){
                this.finished_at=LocalDateTime.now();
            }
            this.status=status;
        }

    }
