--------------------------------------------------------
--  DDL for View VS_FK_CHAIN_00
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VS_FK_CHAIN_00" ("OWNER", "PARENT_TABLE_NAME", "CHILD_TABLE_NAME", "CONSTRAINT_NAME", "COLUMN_NAME", "POSITION", "R_CONSTRAINT_NAME", "R_COLUMN_NAME", "R_POSITION") AS 
  (
select
--
-- Estrattore delle ddl di creazione di FK che afferiscono a parent table
--
-- Version: 1.0
-- Date: 09/06/2001
--
-- History:
--
-- Body:
--
c.owner,
c.table_name,
b.table_name,
b.constraint_name,
a.column_name,
a.position,
c.constraint_name,
d.column_name,
d.position
from
all_cons_columns a,
all_constraints b,
all_constraints c,
all_cons_columns d
where  b.owner=c.owner
and c.constraint_name = b.r_constraint_name
and b.constraint_type = 'R'
and a.owner=b.owner
and a.table_name=b.table_name
and a.constraint_name=b.constraint_name
and d.constraint_name = c.constraint_name
and d.table_name = c.table_name
and d.owner = c.owner
and a.position = d.position
);

   COMMENT ON TABLE "VS_FK_CHAIN_00"  IS '{
 Estrattore delle ddl di creazione di FK che afferiscono a parent table
}';
