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


insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'MAIN', 'ohje-vastuuhenkilolle', 'Ohje aloitteen vastuuhenkilölle', 'Ohje aloitteen vastuuhenkilölle', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 200);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'MAIN', 'anvisningar-for-ansvarspersoner', 'Anvisningar för ansvarspersoner', 'Anvisningar för ansvarspersoner', 'Innehållet uppdateras', 'Innehållet uppdateras', 200);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'MAIN', 'ohje-kannattajalle', '', 'Ohje aloitteen kannattajalle', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 300);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'MAIN', 'anvisningar-for-dem-som-vill-understoda-initiativ', '', 'Anvisningar för dem som vill understöda initiativ', 'Innehållet uppdateras', 'Innehållet uppdateras', 300);

insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE_FI', 'palvelun-tarkoitus', '', 'Palvelun tarkoitus', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 400);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE_FI', 'syftet-med-webbtjansten', '', 'Syftet med webbtjänsten', 'Innehållet uppdateras', 'Innehållet uppdateras', 400);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE_FI', 'palvelun-kayttoehdot', '', 'Palvelun käyttöehdot', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 500);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE_FI', 'anvandarvillkor-for-webbtjansten', '', 'Användarvillkor för webbtjänsten', 'Innehållet uppdateras', 'Innehållet uppdateras', 500);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition, footer_display) values ('FI', 'KUNTALAISALOITE_FI', 'henkilotietojen-suoja-ja-tietoturva', '', 'Henkilötietojen suoja ja tietoturva', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 600, true);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition, footer_display) values ('SV', 'KUNTALAISALOITE_FI', 'skydd-av-personuppgifter-och-dataskydd', '', 'Skydd av personuppgifter och dataskydd', 'Innehållet uppdateras', 'Innehållet uppdateras', 600, true);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE_FI', 'palvelun-avoin-kehittaminen', '', 'Palvelun avoin kehittäminen', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 700);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE_FI', 'oppen-utveckling-av-webbtjansten', '', 'Öppen utveckling av webbtjänsten', 'Innehållet uppdateras', 'Innehållet uppdateras', 700);

insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition, footer_display) values ('FI', 'KUNTALAISALOITE_FI', 'yhteystiedot', '', 'Yhteystiedot', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 800, true);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition, footer_display) values ('SV', 'KUNTALAISALOITE_FI', 'kontaktuppgifter', '', 'Kontaktuppgifter', 'Innehållet uppdateras', 'Innehållet uppdateras', 800, true);


insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE', 'kuntalaisaloite-lyhyesti', '', 'Kuntalaisaloite lyhyesti', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 900);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE', 'kuntalaisaloite-lyhyesti-sv', '', 'Kuntalaisaloite lyhyesti', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 900);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE', 'aloitteen-vaiheet', '', 'Aloitteen vaiheet', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 1000);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE', 'initiativets-skeden', '', 'Initiativets skeden', 'Innehållet uppdateras', 'Innehållet uppdateras', 1000);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('FI', 'KUNTALAISALOITE', 'ukk', '', 'Usein kysyttyjä kysymyksiä', 'Sisältöä päivitetään paraikaa', 'Sisältöä päivitetään paraikaa', 1100);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition) values ('SV', 'KUNTALAISALOITE', 'vanliga-fragor', '', 'Vanliga frågor', 'Innehållet uppdateras', 'Innehållet uppdateras', 1100);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition, footer_display) values ('FI', 'KUNTALAISALOITE', 'briefly-in-english', '', 'Briefly in english', 'The content is currently being updated', 'The content is currently being updated', 1300, true);
insert into info_text (languageCode, category, uri, published_subject, draft_subject, published, draft, orderPosition, footer_display) values ('SV', 'KUNTALAISALOITE', 'briefly_in_english', '', 'Briefly in english', 'The content is currently being updated', 'The content is currently being updated', 1300, true);

insert into schema_version (script) values ('03_infotext.sql');