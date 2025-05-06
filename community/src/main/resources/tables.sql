create table if not exists user
(
    user_id              int auto_increment
        primary key,
    created_at           datetime    not null,
    deleted_at           datetime    null,
    is_deleted           bit         not null,
    modified_at          datetime    null,
    user_age             int         not null,
    user_birth           varchar(9)  not null,
    user_distance        int         not null,
    user_gender          char        not null,
    user_hiking_count    int         not null,
    user_hiking_mountain int         not null,
    user_login_id        varchar(15) not null,
    user_move_time       int         not null,
    user_nickname        varchar(15) not null,
    user_password        text        not null,
    user_point           int         not null,
    user_profile         text        null
);

create table if not exists party
(
    course_id              int          not null,
    guild_id               int          null,
    is_deleted             bit          not null,
    party_id               int auto_increment
        primary key,
    party_is_linked        bit          not null,
    party_max_participants int          not null,
    party_participants     int          not null,
    party_status           char         not null,
    user_id                int          not null,
    created_at             datetime(6)  null,
    deleted_at             datetime(6)  null,
    modified_at            datetime(6)  null,
    party_finished_at      datetime(6)  null,
    party_schedule         datetime(6)  not null,
    party_started_at       datetime(6)  null,
    party_mountain_name    varchar(300) not null,
    party_name             text         not null,
    party_place            text         not null,
    mountain_id            int          not null,
    courses                varchar(255) null
);

create table if not exists party_user
(
    is_deleted             bit         not null,
    party_id               int         not null,
    party_user_best_height int         null,
    party_user_distance    int         null,
    party_user_id          int auto_increment
        primary key,
    party_user_is_success  bit         null,
    party_user_move_time   int         null,
    party_user_status      char        not null,
    user_id                int         not null,
    created_at             datetime(6) null,
    deleted_at             datetime(6) null,
    modified_at            datetime(6) null,
    party_user_started_at  datetime(6) null,
    party_user_points      geometry    null,
    mountain_id            int         not null
);

create table if not exists guild
(
    guild_id         int auto_increment
        primary key,
    created_at       datetime(6)  not null,
    deleted_at       datetime(6)  null,
    guild_gender     char         not null,
    guild_info       text         not null,
    guild_is_private bit          not null,
    guild_max_age    int          not null,
    guild_member     int          not null,
    guild_min_age    int          not null,
    guild_name       varchar(45)  not null,
    guild_profile    varchar(255) null,
    is_deleted       bit          not null,
    modified_at      datetime(6)  not null,
    region_id        int          not null,
    user_id          int          not null
);

create table if not exists guild_request
(
    guild_request_id int auto_increment
        primary key,
    created_at       datetime(6) not null,
    guild_id         int         not null,
    modified_at      datetime(6) not null,
    status           char        not null,
    user_id          int         not null
);

create table if not exists guild_user
(
    guild_user_id int auto_increment
        primary key,
    created_at    datetime(6) not null,
    deleted_at    datetime(6) null,
    guild_id      int         not null,
    is_deleted    bit         not null,
    modified_at   datetime(6) not null,
    user_id       int         not null,
    visited_at    datetime(6) not null
);

create table if not exists mountain
(
    mountain_id          int auto_increment
        primary key,
    mountain_region      char(6)       null,
    mountain_name        varchar(150)  null,
    mountain_address     varchar(600)  null,
    mountain_image       varchar(255)  null,
    mountain_description varchar(3000) null,
    mountain_code        varchar(9)    null,
    mountain_height      decimal(5)    null,
    mountain_views       int default 0 null,
    mountain_top         point         null,
    mountain_top100      tinyint(1)    null
);

create index idx_mountain_name
    on mountain (mountain_name);

create table if not exists course
(
    course_id       int          not null
        primary key,
    mountain_id     int          null,
    mountain_code   varchar(9)   null,
    course_name     varchar(150) null,
    course_distance float        null,
    course_level    varchar(9)   null,
    course_uptime   int          null,
    course_downtime int          null,
    course_points   geometry     null,
    mntn_code       varchar(255) null,
    constraint course_ibfk_1
        foreign key (mountain_id) references mountain (mountain_id)
);

create index mountain_id
    on course (mountain_id);