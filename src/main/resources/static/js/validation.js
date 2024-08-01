function validatePassword() {
    const password = document.getElementById("motDePasse").value;
    const confirmPassword = document.getElementById("confirmation").value;

    if (password !== confirmPassword) {
        alert("Les mots de passe ne correspondent pas.");
        return false;
    }
    return true;
}
function confirmDelete() {
    if (confirm('Êtes-vous sûr de vouloir supprimer votre compte ?')) {
        document.getElementById('deleteForm').submit();
    }
}

document.addEventListener('DOMContentLoaded', function() {
    function confirmRetraitEffectue(articleId) {
        if (confirm('Voulez-vous confirmer le retrait et créditer les points ?')) {
            window.location.href = '/article/' + articleId + '/retrait';
        }
    }
    window.confirmRetraitEffectue = confirmRetraitEffectue;

    const dateFinEncheres = new Date(document.getElementById('dateFinEncheres').innerText);
    const dateDebutEncheres = new Date(document.getElementById('dateDebutEncheres').innerText);
    const now = new Date();
    const btnEncherir = document.getElementById('btn-encherir');
    const btnEditArticle = document.getElementById('btn-edit-article');

    if (now >= dateFinEncheres) {
        btnEncherir.disabled = true;
    }

    if (now >= dateDebutEncheres) {
        btnEditArticle.classList.add('disabled');
        btnEditArticle.href = '#';
    }
});