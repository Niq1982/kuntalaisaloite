create table verified_user_normal_initiatives (
  id bigserial,
  verified_user bigserial constraint verified_user_normal_initiatives_verified_user_nn not null,
  participant bigserial constraint verified_user_normal_initiatives_participant_nn not null,

  constraint verified_user_normal_initiatives_initiative_pk primary key (id),
  constraint verified_user_normal_initiatives_verified_user_id foreign key (verified_user) references verified_user(id) ON DELETE CASCADE,
  constraint verified_user_normal_initiatives_participant_id foreign key (participant) references participant(id) ON DELETE CASCADE
);