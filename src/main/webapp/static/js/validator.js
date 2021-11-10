// не используется
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById("toValidate");
    const inputs = document.querySelectorAll('input[data-rule]');

    function valid(){
        flag = true;
        for(let input of inputs){
            let rule = this.dataset.rule;
            let value = this.value;
            let cheak = false;

            switch (rule) {
                case 'email':
                    regex = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
                    cheak = !regex.test(value);
                    break;
                case 'password':
                    cheak = value.length < 8 || value.length > 32;
                    break;
                case 'nickname':
                    cheak = value.length < 1 || value.length > 20;
                    break;
                case 'link':
                    cheak = value.length < 1 || value.length > 2048;
                    break;
            }

            if(check) {
                flag = false;
                break;
            }
        }
        if(flag){
            form.submit();
        }
    }
});
