-- $ID$
-- TPC-H/TPC-R Large Volume Customer Query (Q18)
-- Function Query Definition
-- Approved February 1998
:x
:o
select
	c_name,
	c_customerkey,
	o_orderskey,
	o_orderdate,
	o_totalprice,
	sum(l_quantity)
from
	tpch1.tpch.customer,
	tpch2.tpch.orders,
	tpch2.tpch.lineitem
where
	o_orderskey in (
		select
			l_orderskey
		from
			tpch2.tpch.lineitem
		group by
			l_orderskey having
				sum(l_quantity) > :1
	)
	and c_customerkey = o_customerkey
	and o_orderskey = l_orderskey
group by
	c_name,
	c_customerkey,
	o_orderskey,
	o_orderdate,
	o_totalprice
order by
	o_totalprice desc,
	o_orderdate;
