function openPopup() {
    document.getElementById("filterPopup").style.display = "block";
}

function closePopup() {
    document.getElementById("filterPopup").style.display = "none";
}

function applyFilters() {
    // Logique pour appliquer les filtres
    closePopup();
}

document.addEventListener('DOMContentLoaded', function() {
    const achatsRadio = document.getElementById('achats');
    const ventesRadio = document.getElementById('ventes');
    const achatsFilter = document.getElementById('achats-filter');
    const ventesFilter = document.getElementById('ventes-filter');

    function toggleFilters() {
        if (achatsRadio.checked) {
            achatsFilter.removeAttribute('disabled');
            ventesFilter.setAttribute('disabled', 'disabled');
        } else if (ventesRadio.checked) {
            ventesFilter.removeAttribute('disabled');
            achatsFilter.setAttribute('disabled', 'disabled');
        }
    }

    // Initial call to set the correct state
    toggleFilters();

    // Add event listeners
    achatsRadio.addEventListener('change', toggleFilters);
    ventesRadio.addEventListener('change', toggleFilters);
})
