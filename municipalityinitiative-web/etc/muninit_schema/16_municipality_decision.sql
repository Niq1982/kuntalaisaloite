alter table municipality_initiative add column municipality_decision text;
alter table municipality_initiative add column municipality_decision_date date;

create table decision_attachment (
  id bigserial,
  description varchar(100) constraint decision_attachment_description_nn not null,
  initiative_id bigserial,

  added timestamp default now(),
  content_type varchar(20) constraint decision_attachment_content_type_nn not null,
  file_type varchar(4) constraint decision_attachment_file_type_nn not null,

  constraint decision_attachment_pk primary key (id),
  constraint decision_attachment_initiative_id foreign key (initiative_id) references municipality_initiative(id)
);

create table municipality_user (
  id bigserial,
  initiative_id bigserial,
  management_hash varchar(40),

  constraint municipality_user_pk primary key (id),
  constraint municipality_user_initiative_id foreign key (initiative_id) references municipality_initiative(id),
  constraint municipality_user_management_hash_u unique(management_hash)

);