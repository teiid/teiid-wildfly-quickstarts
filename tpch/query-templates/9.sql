-- $ID$
-- TPC-H/TPC-R Product Type Profit Measure Query (Q9)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	nation,
	o_year,
	sum(amount) as sum_profit
from
	(
		select
			n_name as nation,
			extract(year from o_orderdate) as o_year,
			l_extendedprice * (1 - l_discount) - ps_supplycost * l_quantity as amount
		from
			tpch2.tpch.part,
			tpch1.tpch.supplier,
			tpch2.tpch.lineitem,
			tpch2.tpch.partsupp,
			tpch2.tpch.orders,
			tpch1.tpch.nation
		where
			s_supplierkey = l_supplierkey
			and ps_supplierkey = l_supplierkey
			and ps_partkey = l_partkey
			and p_partkey = l_partkey
			and o_orderskey = l_orderskey
			and s_nationkey = n_nationkey
			and p_name like '%:1%'
	) as profit
group by
	nation,
	o_year
order by
	nation,
	o_year desc;
