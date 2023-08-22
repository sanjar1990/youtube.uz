create or replace function  view_count_increase_trigger_function()
returns trigger
language plpgsql
AS $$
declare
begin
update video set view_count=view_count+1 where id=new.video_id;
return new;
end; $$ ;

create or replace trigger increase_view_count_trigger
Before insert
on video_watched
for each row
execute procedure view_count_increase_trigger_function()