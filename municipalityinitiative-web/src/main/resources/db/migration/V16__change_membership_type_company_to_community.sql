UPDATE participant
SET membership_type = 'community'
WHERE membership_type = 'company';

UPDATE verified_participant
SET membership_type = 'community'
WHERE membership_type = 'company';
