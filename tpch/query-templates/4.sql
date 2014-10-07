-- $ID$
-- TPC-H/TPC-R Order Priority Checking Query (Q4)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	o_orderpriority,
	count(*) as order_count
from
	tpch2.tpch.orders
where
	o_orderdate >= ':1'
	and o_orderdate < TIMESTAMPADD(SQL_TSI_MONTH,'3',':1') 
	and exists (
		select
			*
		from
			tpch2.tpch.lineitem
		where
			l_orderskey = o_orderskey
			and l_commitdate < l_receiptdate
	)
group by
	o_orderpriority
order by
	o_orderpriority;
