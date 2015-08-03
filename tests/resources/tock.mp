load 'tick.mp';

function tock(n) {
	if (n <= 0) {
		return [];
	}

	return ( 'tock' : tick(n - 1) );
}
