create table attachment (
    id bigserial,
    filename varchar(50) constraint attachment_filename_nn not null,
    initiative_id bigserial,

    added timestamp default now(),

    constraint attachment_pk primary key (id),
    constraint attachment_initiative_id foreign key (initiative_id) references municipality_initiative(id)
);

create index attachment_initiative_id_index on attachment(initiative_id);
