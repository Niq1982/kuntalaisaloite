alter table municipality_initiative add column youth_initiative_id bigint;
alter table municipality_initiative add constraint youth_initiative_id_unique unique(youth_initiative_id);
