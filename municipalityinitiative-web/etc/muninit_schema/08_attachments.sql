create table attachment (
    id bigserial,
    description varchar(100) constraint attachment_filename_nn not null,
    initiative_id bigserial,

    added timestamp default now(),
    content_type varchar(20) constraint attachment_content_type_nn not null,
    file_type varchar(4) constraint attachment_file_type_nn not null,
    accepted boolean constraint attachment_accepted_nn not null default false,

    constraint attachment_pk primary key (id),
    constraint attachment_initiative_id foreign key (initiative_id) references municipality_initiative(id)
);

create index attachment_id_index on attachment(id);
create index attachment_initiative_id_index on attachment(initiative_id);
