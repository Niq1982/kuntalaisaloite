drop type if exists emailAttachmentType;
create type emailAttachmentType as enum ('NONE', 'PARTICIPANTS');

create table email (
    id bigserial,
    initiative_id bigserial,

    recipients varchar(1024) constraint email_recipients_nn not null,
    subject varchar(1024) constraint email_subject_nn not null, -- initiative name is 512,
    body_html text constraint email_body_html_nn not null,
    body_text text constraint email_body_text_nn not null,
    reply_to varchar(50) constraint email_replyto_nn not null,
    sender varchar(50) constraint email_sender_nn not null,
    attachment emailAttachmentType default 'NONE',

    succeeded timestamp,
    last_failed timestamp,
    tried boolean constraint email_tried_nn not null default false,

    constraint email_pk primary key (id),
    constraint email_initiative_id foreign key (initiative_id) references municipality_initiative(id)
    -- TODO: Indexes
);


create index email_tried_index on email(tried);