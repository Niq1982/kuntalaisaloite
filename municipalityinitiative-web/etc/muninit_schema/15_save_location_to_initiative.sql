create table location (
  id bigserial,

  initiative_id bigserial not null,

  location_lat NUMERIC(9, 6) not null,
  location_lng NUMERIC(9, 6) not null,

  constraint location_pk primary key (id),
  constraint location_initiative_id foreign key (initiative_id) references municipality_initiative(id)
);

create index location_initiative_index on location(initiative_id);

