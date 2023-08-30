create or replace function video_like_trigger_function()
returns trigger
language plpgsql
As $$
begin
if TG_OP='INSERT' then
if new.type='LIKE' then
update video set like_count=like_count+1 where id=new.video_id;
elseif new.type='DISLIKE' then
update video set dislike_count=dislike_count+1 where id=new.video_id;
end if;
elseif TG_OP='UPDATE' then
if new.type='LIKE' and old.type='DISLIKE' then
update video set like_count=like_count+1, dislike_count=dislike_count-1 where id=new.video_id;
elseif new.type='DISLIKE' and old.type='LIKE' then
update video set dislike_count=dislike_count+1, like_count=like_count-1 where id=new.video_id;
end if;
elseif TG_OP='DELETE' then
if old.type='LIKE' then
update video set like_count=like_count-1 where id=old.video_id;
elseif old.type='DISLIKE' then
update video set dislike_count=dislike_count-1 where id=old.video_id;
end if;
return old;
end if;
return new;
end; $$ ;

create or replace trigger video_like_trigger
before insert or update  or delete
                 on video_like
                     for each row
                     execute procedure video_like_trigger_function();