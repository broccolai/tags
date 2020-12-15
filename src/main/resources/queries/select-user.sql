SELECT uuid, id, owner
FROM tags_user
    LEFT JOIN tags_tags
ON tags_user.uuid = tags_tags.owner
WHERE tags_user.uuid = :uuid;
