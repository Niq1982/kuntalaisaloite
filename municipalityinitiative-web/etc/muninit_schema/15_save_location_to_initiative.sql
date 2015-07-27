alter table municipality_initiative add column location_lat NUMERIC(9, 6);
alter table municipality_initiative add column location_lng NUMERIC(9, 6);
alter table municipality_initiative add column location_description text;