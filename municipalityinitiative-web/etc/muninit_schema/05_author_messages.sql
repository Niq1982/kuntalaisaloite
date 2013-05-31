create table author_message (
    id bigserial,
    initiative_id bigserial,
    contactor varchar(100) constraint authorMessage_contactor_nn not null,
    contactor_email varchar(100) constraint authorMessage_contactorEmail_nn not null,
    message varchar(2048) constraint authorMessage_message_nn not null,
    confirmation_code varchar(40) constraint authorMessage_confirmationCode_nn not null,

    constraint authorMessage_pk primary key (id),
    constraint authorMessage_initiativeId_fk foreign key (initiative_id) references municipality_initiative(id)
);