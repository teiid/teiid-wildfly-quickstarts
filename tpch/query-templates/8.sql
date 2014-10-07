-- $ID$
-- TPC-H/TPC-R National Market Share Query (Q8)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	o_year,
	sum(case
		when nation = ':1' then volume
		else 0
	end) / sum(volume) as mkt_share
from
	(
		select
			extract(year from o_orderdate) as o_year,
			l_extendedprice * (1 - l_discount) as volume,
			n2.n_name as nation
		from
			tpch2.tpch.part,
			tpch1.tpch.supplier,
			tpch2.tpch.lineitem,
			tpch2.tpch.orders,
			tpch1.tpch.customer,
			tpch1.tpch.nation n1,
			tpch1.tpch.nation n2,
			tpch1.tpch.region
		where
			p_partkey = l_partkey
			and s_supplierkey = l_supplierkey
			and l_orderskey = o_orderskey
			and o_customerkey = c_customerkey
			and c_nationkey = n1.n_nationkey
			and n1.n_regionkey = r_regionkey
			and r_name = ':2'
			and s_nationkey = n2.n_nationkey
			and o_orderdate between '1995-01-01' and '1996-12-31'
			and p_type = ':3'
	) as all_nations
group by
	o_year
order by
	o_year;
