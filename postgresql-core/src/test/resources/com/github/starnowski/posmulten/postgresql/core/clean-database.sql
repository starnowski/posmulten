DELETE FROM public.comments CASCADE;
DELETE FROM non_public_schema.comments CASCADE;
DELETE FROM public.posts CASCADE;
DELETE FROM non_public_schema.posts CASCADE;
DELETE FROM public.users CASCADE;
DELETE FROM non_public_schema.users CASCADE;
