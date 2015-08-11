create type emailReportType as enum('IN_ACCEPTED','QUARTER_REPORT');

alter table municipality_initiative add column last_email_report_type emailReportType;
alter table municipality_initiative add column last_email_report_time timestamp;
alter table municipality_initiative add constraint municipality_initiative_report_time_not_null check (last_email_report_time is not null or last_email_report_type is null);
