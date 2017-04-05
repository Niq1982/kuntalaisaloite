-- Verified participants that participate to normal initiatives will be moved to verified participants table.

ALTER TABLE verified_participant
  ADD COLUMN membership_type membershipType CONSTRAINT verified_participant_membership_type_nn NOT NULL DEFAULT 'none';

ALTER TABLE verified_participant
  ADD COLUMN municipality_id BIGINT;

alter table verified_participant add constraint verified_participant_municipality_id_fk foreign key (municipality_id) REFERENCES municipality(id);
