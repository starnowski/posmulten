UPDATE public.comments SET parent_comment_id = NULL, parent_comment_user_id = NULL;
UPDATE non_public_schema.comments SET parent_comment_id = NULL, parent_comment_user_id = NULL;
DELETE FROM public.comments CASCADE;
DELETE FROM non_public_schema.comments CASCADE;
DELETE FROM public.posts CASCADE;
DELETE FROM non_public_schema.posts CASCADE;
DELETE FROM public.users CASCADE;
DELETE FROM non_public_schema.users CASCADE;
