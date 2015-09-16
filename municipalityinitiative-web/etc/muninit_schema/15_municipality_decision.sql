alter table municipality_initiative add column municipality_decision text;


create table decision_attachment (
  id bigserial,
  description varchar(100) constraint attachment_description_nn not null,
  initiative_id bigserial,

  added timestamp default now(),
  content_type varchar(20) constraint attachment_content_type_nn not null,
  file_type varchar(4) constraint attachment_file_type_nn not null,

  constraint attachment_pk primary key (id),
  constraint attachment_initiative_id foreign key (initiative_id) references municipality_initiative(id)
);