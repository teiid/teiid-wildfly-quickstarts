-- $ID$
-- TPC-H/TPC-R Important Stock Identification Query (Q11)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	ps_partkey,
	sum(ps_supplycost * ps_availqty) as v
from
	tpch2.tpch.partsupp,
	tpch1.tpch.supplier,
	tpch1.tpch.nation
where
	ps_supplierkey = s_supplierkey
	and s_nationkey = n_nationkey
	and n_name = ':1'
group by
	ps_partkey having
		sum(ps_supplycost * ps_availqty) > (
			select
				sum(ps_supplycost * ps_availqty) * :2
			from
				tpch2.tpch.partsupp,
				tpch1.tpch.supplier,
				tpch1.tpch.nation
			where
				ps_supplierkey = s_supplierkey
				and s_nationkey = n_nationkey
				and n_name = ':1'
		)
order by
	v desc;
