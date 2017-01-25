create table notification (
  lock boolean unique check (lock = true) default true,
  fi varchar(10000),
  sv varchar(10000),
  urlFi varchar(1000),
  urlSv varchar(1000),
  urlFiText varchar(1000),
  urlSvText varchar(1000),
  enabled boolean not null
);