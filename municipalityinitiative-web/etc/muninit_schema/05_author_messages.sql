create table author_message (
    confirmation_code varchar(40) constraint authorMessage_confirmationCode_nn not null, --- TODO: CHAR
    initiative_id bigserial,
    contactor varchar(100) constraint authorMessage_contactor_nn not null,
    contactor_email varchar(100) constraint authorMessage_contactorEmail_nn not null,
    message varchar(2048) constraint authorMessage_message_nn not null,

    constraint authorMessage_pk primary key (confirmation_code),
    constraint authorMessage_initiativeId_fk foreign key (initiative_id) references municipality_initiative(id)
);

create index authorMessage_pk_index on author_message(confirmation_code);