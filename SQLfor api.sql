USE [ApiDB]
GO

SELECT [id]
      ,[web_pages]
      ,[state_province]
      ,[alpha_two_code]
      ,[name]
      ,[country]
      ,[domins]
  FROM [dbo].[ApiResults]

  delete  from ApiResults where id is not null;
   Update  ApiResults SET web_pages= 'http://www.Rawd.ac.uk/',
   state_province= 'SEEB',
   alpha_two_code= '1001' ,
   name= 'RAWDHA',
   country= 'OMAN',
   domins= 'Something' 
   Where id = 2
   SELECT * FROM ApiResults WHERE id = 8755;
GO


