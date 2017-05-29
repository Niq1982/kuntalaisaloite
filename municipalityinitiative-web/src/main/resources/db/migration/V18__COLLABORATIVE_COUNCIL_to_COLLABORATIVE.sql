UPDATE municipality_initiative
SET type = 'COLLABORATIVE'
WHERE type = 'COLLABORATIVE_COUNCIL' and state = 'PUBLISHED';

UPDATE municipality_initiative
SET type = 'UNDEFINED'
WHERE type = 'COLLABORATIVE_COUNCIL' and state != 'PUBLISHED';