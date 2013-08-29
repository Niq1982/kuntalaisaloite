drop type if exists languageCode;
create type languageCode as enum ('FI', 'SV');
drop type if exists infoTextCategory;
create type infoTextCategory as enum ('MAIN', 'KUNTALAISALOITE', 'KUNTALAISALOITE_FI', 'TIEDOTTEET');

create table info_text (
    id bigserial,
    languageCode languageCode constraint infoText_languageCode_nn not null,
    category infoTextCategory constraint infoText_infoTextCategory_nn not null,
    footer_display boolean constraint infoText_footerDisplay_nn not null default false,
    uri varchar(100),
    published_subject varchar(100),
    draft_subject varchar(100),
    published text,
    draft text,
    modifier varchar(100),
    modified timestamp,

    orderPosition integer constraint infoText_orderPosition_nn not null,

    constraint infoText_pk primary key (id),
    constraint infoText_uri_u unique (uri),
    constraint infoText_orderPosition_u unique (languageCode, category, orderPosition)
);

insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'TIEDOTTEET', 'tiedotteet', 'Tiedotteet', 'Tiedotteet', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 100);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'TIEDOTTEET', 'aktuellt', 'Aktuellt', 'Aktuellt', 'Innehållet uppdateras', 'Innehållet uppdateras', 100);

insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'MAIN', 'ohjeet-vastuuhenkilolle', 'Ohjeet aloitteen vastuuhenkilölle', 'Ohjeet aloitteen vastuuhenkilölle', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 200);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'MAIN', 'anvisningar-for-ansvarspersoner', 'Anvisningar för ansvarspersoner', 'Anvisningar för ansvarspersoner', 'Innehållet uppdateras', 'Innehållet uppdateras', 200);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'MAIN', 'ohje-osallistujalle', '', 'Ohje aloitteeseen osallistujalle', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 300);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'MAIN', 'anvisningar-for-deltagare-i-ett-initiativ', '', 'Anvisningar för deltagare i ett initiativ', 'Innehållet uppdateras', 'Innehållet uppdateras', 300);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'MAIN', 'tietoa-kunnille', '', 'Tietoa kunnille', 'Innehållet uppdateras', 'Sisältöä päivitetään paraikaa', 400);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'MAIN', 'information-till-kommunerna', '', 'Tietoa kunnille', 'Innehållet uppdateras', 'Innehållet uppdateras', 400);


insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE_FI', 'palvelun-tarkoitus', '', 'Palvelun tarkoitus', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 500);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE_FI', 'andamalet-med-tjansten', 'Ändamålet med tjänsten', 'Ändamålet med tjänsten', 'Innehållet uppdateras', 'Innehållet uppdateras', 500);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE_FI', 'palvelun-kayttoehdot', '', 'Palvelun käyttöehdot', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 600);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE_FI', 'anvandarvillkor', '', 'Användarvillkor', 'Innehållet uppdateras', 'Innehållet uppdateras', 600);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE_FI', 'henkilotietojen-suoja-ja-tietoturva', '', 'Henkilötietojen suoja ja tietoturva', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 700);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE_FI', 'skydd-av-personuppgifter-och-dataskydd', '', 'Skydd av personuppgifter och dataskydd', 'Innehållet uppdateras', 'Innehållet uppdateras', 700);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE_FI', 'palvelun-avoin-kehittaminen', '', 'Palvelun avoin kehittäminen', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 800);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE_FI', 'utveckling-av-tjansten', '', 'Utveckling av tjänsten', 'Innehållet uppdateras', 'Innehållet uppdateras', 800);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE_FI', 'yhteystiedot', '', 'Yhteystiedot', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 900);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE_FI', 'kontaktuppgifter', '', 'Kontaktuppgifter', 'Innehållet uppdateras', 'Innehållet uppdateras', 900);


insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE', 'kuntalaisaloite-lyhyesti', '', 'Kuntalaisaloite lyhyesti', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 1000);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE', 'kort-om-invanarinitiativ', '', 'Kort om invånarinitiativ', 'Innehållet uppdateras', 'Innehållet uppdateras', 1000);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE', 'kuntalaisaloitteen-muodot', '', 'Kuntalaisaloitteen muodot', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 1100);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE', 'formen-av-ett-invanarinitiativ', '', 'Formen av ett invånarinitiativ', 'Innehållet uppdateras', 'Innehållet uppdateras', 1100);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE', 'ukk', '', 'Usein kysyttyjä kysymyksiä', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 1200);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE', 'faq', '', 'Vanliga frågor om medborgarinitiativ', 'Innehållet uppdateras', 'Innehållet uppdateras', 1200);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE', 'muita-aloitemuotoja', '', 'Muita aloitemuotoja', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 1300);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE', 'andra-former-av-initiativ', '', 'Andra former av initiativ', 'Innehållet uppdateras', 'Innehållet uppdateras', 1300);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE', 'briefly-in-english', '', 'Briefly in english', 'The content is currently being updated', 'The content is currently being updated', 1400);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE', 'briefly_in_english', '', 'Briefly in english', 'The content is currently being updated', 'The content is currently being updated', 1400);

insert into schema_version (script) values ('03_infotext.sql');