create table follow_initiative (
  id bigserial,
  email varchar(100) not null,
  initiative_id bigserial constraint follow_initiative_initiative_id_nn not null,
  unsubscribe_hash varchar(40) constraint unsubscribe_hash_nn not null,

  constraint follow_initiative_id primary key (id),
  constraint follow_initiative_initiative_id foreign key (initiative_id) references municipality_initiative(id),
  constraint follow_initiative_unsubscribe_hash unique(unsubscribe_hash)
);


create index follow_initiative_index on follow_initiative(initiative_id);