
$(document).ready(function() {

  $("#role-selector").change(function() {
    $("#inputRole").val($(this).val());
  });

  $("#category-selector").change(function() {
    $("#inputCategory").val($(this).val());
  });

  $('#add-book-entry-button').click(function(){
    $('.create-book-entry').slideToggle();
    $(this).slideToggle();
  });

  $('.book-entry-edit').click(function(){
    $("#entryId").val($(this).data('id'));
    $("#editInventoryNumber").val($(this).data('inventorynumber'));
    $("#editSignature").val($(this).data('signature'));
    $("#editPhysicalState").val($(this).data('physicalstate'));
    $('#bookEntryEditModal').modal('show');
  });

  if($('#editEntryFormErrors').length) {
    $('#bookEntryEditModal').modal('show');
  }

  $('.book-entry-remove').click(function() {
    $("#removeEntryId").val($(this).data('id'));
    $('#bookEntryRemoveModal').modal('show');
  });

  $('#confirmRemoveBookEntry').click(function(){
    $('#removeBookEntryForm').submit();
  });

  $('.end-rent-button').click(function(){
    $('#rentEndingModal').modal('show');
    $("#rentId").val($(this).data('rentid'));
    $("#rent-modal-book-title").html($(this).data('booktitle'));
    $("#rent-modal-book-physical-state").html($(this).data('physicalstate'));
  });

  $("#confirmEndRentButton").click(function(){
    $("#endRentForm").submit();
  });

  $(".udpate-physical-state").click(function(){
    $(".update-physical-state-block").toggleClass("display-none");
    $(this).slideToggle();
    $(".udpate-physical-state-info").slideToggle();
  });

  $("#findByIsbnInput").keypress(function(event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13') {
    	$.ajax({
        type: "POST",
        url: "/books/findBook",
        data: {
          'isbn': $(this).val()
        },
        success: function(response)
        {
          $("#findByIsbnInput").val('');
          $("#searchBookWidgetBody").html(response);
          $('#searchBookWidget').modal('show');
        }
      });
    }
    event.stopPropagation();
  });

  $("#searchBookWidget").on('click', '#confirmBookSelection',function() {
    let selectedBook = $('input[name=selectedBook]:checked');
    if(selectedBook.length > 0){
      $("#inputIsbn").val(selectedBook.data('isbn'));
      $("#inputAuthor").val(selectedBook.data('author'));
      $("#inputPublisher").val(selectedBook.data('publisher'));
      $("#inputTitle").val(selectedBook.data('title'));
      $("#inputpublicationYear").val(selectedBook.data('publicationyear'));
      $('#searchBookWidget').modal('hide');
    }
  });


  $(".barcode-input-holder").keypress(function(event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13') {
      $(this).parent().submit();
    }
  });

  $(".department-name").click(function() {
    var departmentId = $(this).data("departmentid");
    $.ajax({
      type: "POST",
      url: "/books/departmentInfo",
      data: {
        'departmentId': departmentId
      },
      success: function(response)
      {
        $("#departmentInfoModalBody").html(response);
        $('#departmentInfoModal').modal('show');
      }
    });
  })
});
