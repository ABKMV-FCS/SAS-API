DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
GRANT ALL ON SCHEMA public TO yourusername;

-- start application to create tables

INSERT INTO public.roles(name) VALUES('ROLE_FACULTY');
INSERT INTO public.roles(name) VALUES('ROLE_STUDENT');
INSERT INTO public.roles(name) VALUES('ROLE_ADMIN');
