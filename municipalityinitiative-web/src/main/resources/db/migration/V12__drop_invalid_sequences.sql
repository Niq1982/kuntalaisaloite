-- For probably copypasting reasons all bigints are created as bigserials
-- This might cause bad behaviour. Let's drop all those secuences that should not be present.

drop sequence municipality_initiative_municipality_id_seq CASCADE;
drop sequence attachment_initiative_id_seq CASCADE;
drop sequence author_initiative_id_seq CASCADE;
drop sequence author_invitation_initiative_id_seq CASCADE;
drop sequence author_message_initiative_id_seq CASCADE;
drop sequence decision_attachment_initiative_id_seq CASCADE;
drop sequence email_initiative_id_seq CASCADE;
drop sequence follow_initiative_initiative_id_seq CASCADE;
drop sequence location_initiative_id_seq CASCADE;
drop sequence municipality_user_initiative_id_seq CASCADE;
drop sequence participant_municipality_initiative_id_seq CASCADE;
drop sequence participant_municipality_id_seq CASCADE;
drop sequence review_history_initiative_id_seq CASCADE;
drop sequence verified_author_initiative_id_seq CASCADE;
drop sequence verified_author_verified_user_id_seq CASCADE;
drop sequence verified_participant_initiative_id_seq CASCADE;
drop sequence verified_participant_verified_user_id_seq CASCADE;
drop sequence verified_user_municipality_id_seq CASCADE;