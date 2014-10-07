-- $ID$
-- TPC-H/TPC-R Potential Part Promotion Query (Q20)
-- Function Query Definition
-- Approved February 1998
:x
:o
select
	s_name,
	s_address
from
	tpch1.tpch.supplier,
	tpch1.tpch.nation
where
	s_supplierkey in (
		select
			ps_supplierkey
		from
			tpch2.tpch.partsupp
		where
			ps_partkey in (
				select
					p_partkey
				from
					tpch2.tpch.part
				where
					p_name like ':1%'
			)
			and ps_availqty > (
				select
					0.5 * sum(l_quantity)
				from
					tpch2.tpch.lineitem
				where
					l_partkey = ps_partkey
					and l_supplierkey = ps_supplierkey
					and l_shipdate >=  ':2'
					and l_shipdate <  TIMESTAMPADD(SQL_TSI_YEAR,'1', ':2')
			)
	)
	and s_nationkey = n_nationkey
	and n_name = ':3'
order by
	s_name;
