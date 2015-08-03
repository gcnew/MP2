array_join = (a, s) => {
	s ||= '';

	local retval = '';
	local i = a.iterator();

	if (!i.hasNext()) {
		return retval;
	}

	retval += i.next();
	while (i.hasNext()) {
		retval += s + i.next();
	}

	return retval;
};
